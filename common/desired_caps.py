from appium import webdriver
import logging.config
import yaml
import os
from time import sleep
from common.variable import GetVariable


logging.config.fileConfig('../config/log.conf')
logging = logging.getLogger()


def desired_caps():
    with open("../config/desired_caps.yaml", 'r', encoding='utf-8') as f:
        data = yaml.load(f)

    caps = dict()

    caps['platformVersion'] = GetVariable.device["platformVersion"]
    caps['udid'] = GetVariable.device["udid"]
    caps['deviceName'] = GetVariable.device["deviceName"]

    caps['platformName'] = data['platformName']
    caps['appPackage'] = data['appPackage']
    caps['appActivity'] = data['appActivity']
    caps['unicodeKeyboard'] = data['unicodeKeyboard']
    caps['resetKeyboard'] = data['resetKeyboard']
    caps['automationName'] = data['automationName']
    caps['noReset'] = data['noReset']

    base_path = os.path.dirname(os.path.dirname(__file__))
    file_path = os.path.join(base_path, 'app', 'kaoyan.apk')
    caps['app'] = file_path

    driver = webdriver.Remote('http://{}:{}/wd/hub'.format(data["ip"], GetVariable.port), caps)
    driver.implicitly_wait(2)

    sleep(5)

    return driver





# if __name__ == '__main__':
#     # for desired in desired_process:
#     #     desired.start()
#     # for desired in desired_process:
#     #     desired.join()
