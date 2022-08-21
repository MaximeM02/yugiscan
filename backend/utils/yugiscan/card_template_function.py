from typing import List, Optional
from models.databases.yugiscan import CardTemplate
from models.dataclass.card_template import CardTemplateModel
from core import core, DB

def select_card_template() -> Optional[List[CardTemplateModel]]:
    return DB.select(
                model=CardTemplate,
                dataclass=CardTemplateModel
            )