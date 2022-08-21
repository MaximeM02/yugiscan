from alphaz.models.tests import AlphaTest, test
from alphaz.libs import api_lib

from core import core

LOG = core.get_logger("tests")

class CardTemplateTest(AlphaTest):
    def __init__(self) -> None:
        super().__init__()

        self.url = "/yugiscan/card_template"

    @test(description="Test get")
    def test_get(self):

        expected_data = [{'id': 1, 'name': 'trap'}, {'id': 2, 'name': 'skill'}, {'id': 3, 'name': 'link_monster'}, {'id': 4, 'name': 'duelist card name'}, {'id': 5, 'name': 'monster'}, {'id': 6, 'name': 'token'}, {'id': 7, 'name': 'magic'}, {'id': 8, 'name': 'xyz'}, {'id': 9, 'name': 'pendule'}, {'id': 10, 'name': ''}]

        answer = api_lib.get_api_answer(
            url=self.url,
            log=LOG,
            params={}
        )

        self.assert_equal(answer.data, expected_data)