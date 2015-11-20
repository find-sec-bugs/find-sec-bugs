/**
 * Find Security Bugs
 * Copyright (c) Philippe Arteau, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */
package com.h3xstream.findbugs.cli;

import com.h3xstream.findsecbugs.FindSecBugsGlobalConfig;
import edu.umd.cs.findbugs.CheckBcel;
import edu.umd.cs.findbugs.FindBugs;
import edu.umd.cs.findbugs.FindBugs2;
import edu.umd.cs.findbugs.PluginException;
import edu.umd.cs.findbugs.TextUICommandLine;
import edu.umd.cs.findbugs.Version;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FsbCommandLineLauncher {


    public static void main(String[] args) throws Exception {
        new FsbCommandLineLauncher().start(args);
    }

    public void start(String[] args) throws IOException, InterruptedException, PluginException {
        if(!CheckBcel.check()) {
            System.exit(1);
        }

        System.out.println(IOUtils.toString(getClass().getResourceAsStream("banner.txt")));

        FindBugs2 findBugs = new FindBugs2();

        TextUICommandLine commandLine = new TextUICommandLine();

        //Some specific initialisation
        FindSecBugsGlobalConfig.getInstance().setPrintCustomInjectionWarning(false);

        //Inject default arguments
        List<String> newArgs = new ArrayList<String>();
        newArgs.addAll(Arrays.asList(args));
        if(!newArgs.contains("-verbose"))
            newArgs.add(0, "-quiet");

        //Process arguments
        FindBugs.processCommandLine(commandLine, newArgs.toArray(new String[newArgs.size()]), findBugs);

        boolean justPrintConfiguration = commandLine.justPrintConfiguration();
        if(!justPrintConfiguration && !commandLine.justPrintVersion()) {
            //Actual execution
            FindBugs.runMain(findBugs, commandLine);
        } else {
            //Informational
            Version.printVersion(justPrintConfiguration);
            System.out.println("FindSecurityBugs "+FindSecBugsGlobalConfig.getInstance().getFindSecBugsVersion());
        }
    }

}
