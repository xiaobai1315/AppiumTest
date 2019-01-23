from baseView.myUnitTest import MyUnitTest
from businessView.login import Login
from common.desired_caps import logging
import unittest


class LoginTest(MyUnitTest):

    def login_case(self, index):
        l = Login(self.driver)
        logging.info("driver = " + str(self.driver))
        data = l.account_data(index)
        if data is None:
            logging.error("test data is none")

        l.login(data[0], data[1])
        if data[2] is not None:
            self.assertTrue(l.check_toast_info(data[2]))
            self.assertFalse(l.check_login_status())
            return

        self.assertFalse(l.check_toast_info(data[2]))
        self.assertTrue(l.check_login_status())

    def test_login1(self):
        self.login_case(0)

    # def test_login2(self):
    #     self.login_case(1)


if __name__ == '__main__':
    unittest.main()
