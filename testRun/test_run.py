import sys
path = r'/Users/jermy/Documents/GitHub/KYBTest/AppiumTest'
sys.path.append(path)


from common.BSTestRunner import BSTestRunner
import unittest
import logging
import time
import multiprocessing
import os
import re
from common.variable import GetVariable


def run_case(my_device, my_port):

    GetVariable.device = my_device
    GetVariable.port = my_port

    test_dir = "../testCase"
    report_dir = "../testReport"

    discover = unittest.defaultTestLoader.discover(test_dir, pattern="test*.py")
    now = time.strftime("%Y-%m-%d %H:%M:%S")
    report_name = report_dir + "/" + now + my_device["udid"] + " test_report.html"

    with open(report_name, 'wb') as f:
        runner = BSTestRunner(stream=f, title="test report", description="app test report")
        logging.info("start run test Case")
        runner.run(discover)


# 获取已连接设备的UDID、deviceName、platformVersion
def connectd_device_udid():
    _list = []

    # 获取已连接安卓设备的udid
    udid = os.popen('adb devices').read()  # 获取已连接设备列表
    for item in re.findall(r"\n.*\t", udid):  # 通过正则表达式获取到每一个设备udid，strip去掉udid前后的空格、换行
        device = {}

        _udid = str(item.strip())
        device["udid"] = str(_udid)
        logging.info("device UDID:{}".format(_udid))

        # 根据UDID获取设备名称
        deviceName = os.popen('adb -s {} shell getprop ro.product.model'.format(_udid)).read()
        logging.info("deviceName:{}".format(deviceName))
        device["deviceName"] = str(deviceName)

        # 根据UDID获取系统版本
        platformVersion = os.popen('adb -s {} shell getprop ro.build.version.release'.format(_udid)).read()
        logging.info("platformVersion:{}".format(platformVersion))
        device["platformVersion"] = str(platformVersion)

        # # 根据UDID获取手机厂商名称
        # deviceName = os.popen('adb -s {} shell getprop ro.product.name'.format(_udid)).read()
        # print(deviceName)

        _list.append(device)

    return _list


device_list = connectd_device_udid()
desired_process = []
for i in range(len(device_list)):
    port = 4723 + i * 2
    device = device_list[i]

    desired = multiprocessing.Process(target=run_case, args=(device, port))
    desired_process.append(desired)


for process in desired_process:
    process.start()

for process in desired_process:
    process.join()