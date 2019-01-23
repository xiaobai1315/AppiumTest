from baseView.baseView import BaseView
from selenium.webdriver.common.by import By
from common.desired_caps import logging
from selenium.common.exceptions import NoSuchElementException
from time import sleep


class Common_Funcs(BaseView):

    cancel_button = (By.ID, "android:id/button2")
    skip_button = (By.ID, "com.tal.kaoyan:id/tv_skip")

    def cancel_update(self):
        print('cancel_update')
        try:
            element = self.find_element(*self.cancel_button)
        except NoSuchElementException:
            print('no cancel button')
            logging.info("no cancel button")
        else:
            print('cancel_update click')
            element.click()

    def skip(self):
        print('skip')
        try:
            element = self.find_element(*self.skip_button)
        except NoSuchElementException:
            print('no skip button')
            logging.info("no skip button")
        else:
            print('skip click')
            element.click()


if __name__ == '__main__':
    from common.desired_caps import desired_caps

    common = Common_Funcs(desired_caps())

    common.cancel_update()
    common.skip()
