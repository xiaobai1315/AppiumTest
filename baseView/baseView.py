import time
import csv


class BaseView(object):
    def __init__(self, driver):
        self.driver = driver

    def find_element(self, *algs):
        return self.driver.find_element(*algs)

    @staticmethod
    def current_time():
        return time.strftime("%Y-%m-%d %H:%M:%S")

    def screen_shot(self, model_name):
        self.driver.save_screenshot('../screen_shot/{}_{}.png'.format(model_name, self.current_time()))

    def account_data(self, index):
        _index = 0
        with open("../data/account.csv") as f:
            reader = csv.reader(f)
            for row in reader:
                if _index == index:
                    return row
                _index += 1
