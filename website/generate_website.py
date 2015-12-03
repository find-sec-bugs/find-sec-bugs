from jinja2 import Environment, FileSystemLoader
import os
import xml.etree.ElementTree as ET

# Capture our current directory
THIS_DIR = os.path.dirname(os.path.abspath(__file__))

class Rule:
    def __init__(self,name,description,resolution,link_id):
        self.name = name
        self.description = description
        self.resolution = resolution
        self.link_id = link_id
    def __str__(self):
        short_desc = (self.description.strip()[:50] + '..') if len(self.description) > 50 else self.description.strip()
        return self.name + ": " + short_desc.replace("\n","").replace("\r","")


def build_template(page, **model):
    j2_env = Environment(loader=FileSystemLoader(THIS_DIR),
                         trim_blocks=True)
    
    with open("out/"+page, "wb") as fh:
        fh.write(j2_env.get_template(page).render(model))

def model_get_rules():
    rulesFile = "../SecurityGuardPlugin/Rules.xml"
    e = ET.parse(rulesFile).getroot()

    rules = []
    
    for rule in e.findall('Rule'):
        rule = Rule(rule.find("Name").text,
            rule.find("Resolution").text or "No resolution available.",
            rule.find("Description").text or "No description available.",
            rule.find("Url").text or "")
        rules.append(rule)
        
        print(rule)
    return rules