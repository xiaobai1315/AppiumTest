from appium import webdriver
import logging.config
import yaml
import os


logging.config.fileConfig('../config/log.conf')
logging = logging.getLogger()


def desired_caps():
    with open("../config/desired_caps.yaml", 'r', encoding='utf-8') as f:
        data = yaml.load(f)

    desired_caps = {}
    desired_caps['platformName'] = data['platformName']
    desired_caps['platformVersion'] = data['platformVersion']
    desired_caps['udid'] = data['udid']
    desired_caps['deviceName'] = data['deviceName']
    desired_caps['appPackage'] = data['appPackage']
    desired_caps['appActivity'] = data['appActivity']
    desired_caps['unicodeKeyboard'] = data['unicodeKeyboard']
    desired_caps['resetKeyboard'] = data['resetKeyboard']
    desired_caps['automationName'] = data['automationName']
    desired_caps['resetKeyboard'] = data['resetKeyboard']
    desired_caps['noReset'] = data['noReset']

    basePath = os.path.dirname(os.path.dirname(__file__))
    filePath = os.path.join(basePath, 'app', 'kaoyan.apk')
    desired_caps['app'] = filePath

    driver = webdriver.Remote('http://{}:{}/wd/hub'.format(data["ip"], data['port']), desired_caps)
    return driver


if __name__ == '__main__':
    desired_caps()
    logging.info('---------info--------')