import subprocess
from time import ctime
import multiprocessing


def appium_start(host, port):
    bootstrap_port = str(port + 1)
    cmd = 'appium -a ' + host + ' -p ' + str(port) + ' -bp ' + str(bootstrap_port)
    print('{} at {}'.format(cmd, ctime()))
    subprocess.Popen(cmd, shell=True, stdout=open('../logs/'+str(port)+'.log', 'a'), stderr=subprocess.STDOUT)


appium_process = []
for i in range(2):
    host = '127.0.0.1'
    port = 4723 + i * 2
    process = multiprocessing.Process(target=appium_start, args=(host, port))
    appium_process.append(process)

if __name__ == '__main__':
    for appium in appium_process:
        appium.start()
    for appium in appium_process:
        appium.join()

