/**
 * <p>
 * Trick to be able to find where the tests dependencies are compiled. This is not a API that is tested.
 * </p>
 * <p>
 * When we are testing samples (package testcode), each classes being analysed is manually loaded.
 * For the test dependencies, we want to include them by default in the auxiliary classes.
 * To do so, we need to pass to FindBugsLaucher a reference to plugin-deps compiled directory.
 * This dummy class (PluginDepsClassPathFinder) is use to determined this location.
 * </p>
 *
 * @See {@code com.h3xstream.findbugs.test.service.FindBugsLauncher#analyze}
 */
public class PluginDepsClassPathFinder {

}
