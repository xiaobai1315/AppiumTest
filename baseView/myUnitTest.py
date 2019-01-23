import unittest
from common.desired_caps import desired_caps, logging
from time import sleep
import warnings


class MyUnitTest(unittest.TestCase):

    def setUp(self):
        warnings.simplefilter("ignore", ResourceWarning)
        logging.info("setup test case")
        self.driver = desired_caps()
        logging.info("drover = " + str(self.driver))

    def tearDown(self):
        logging.info("tear down")
        sleep(5)
        self.driver.close_app()
