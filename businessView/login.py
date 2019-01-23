from common.desired_caps import logging
from common.common_funcs import Common_Funcs
from selenium.webdriver.common.by import By
from selenium.common.exceptions import NoSuchElementException, TimeoutException
from selenium.webdriver.support.ui import WebDriverWait
import csv


class Login(Common_Funcs):
    # 登录用户名、密码、登录按钮
    usernameTF = (By.ID, 'com.tal.kaoyan:id/login_email_edittext')
    pwdTF = (By.ID, 'com.tal.kaoyan:id/login_password_edittext')
    loginBtn = (By.ID, 'com.tal.kaoyan:id/login_login_btn')

    # 检测是否登录成功
    myselfButton = (By.ID, 'com.tal.kaoyan:id/mainactivity_button_mysefl')

    # 使用用户名和密码登录
    def login(self, user_name, password):
        self.cancel_update()
        self.skip()

        logging.info("login with username:{} password:{}".format(user_name, password))
        self.find_element(*self.usernameTF).send_keys(user_name)
        self.find_element(*self.pwdTF).send_keys(password)
        self.find_element(*self.loginBtn).click()
        self.screen_shot("login")

    # 检查是否登录成功
    def check_login_status(self):
        try:
            self.find_element(*self.myselfButton)
        except NoSuchElementException:
            logging.info("login fail")
            return False
        else:
            logging.info("login success")
            return True

    # 登录失败时，检测toast信息是否正确
    def check_toast_info(self, toast):
        # 抓取toast信息
        path = "//*[@text='{}']".format(toast.strip())
        try:
            WebDriverWait(self.driver, 5).until(lambda x: x.find_element_by_xpath(path))
        except NoSuchElementException:
            logging.info("not find toast with message:{}".format(toast))
            return False
        except TimeoutException:
            logging.info("not find toast with message:{}".format(toast))
            return False
        else:
            logging.info("find toast with message:{}".format(toast))
            return True


# if __name__ == '__main__':
#     login = Login(desired_caps())
#     login.cancel_update()
#     login.skip()
#
#     with open("../data/account.csv", "r", encoding="utf-8") as f:
#         reader = csv.reader(f)
#
#         for row in reader:
#             print(row[0])
#             print(row[1])
#
#         login.login(row[0], row[1])
#
#         login.check_toast_info(row[2])
#
#         if login.check_login_status():
#             logging.info("login success")
#         else:
#             logging.info("login fail")

