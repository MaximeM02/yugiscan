from datetime import datetime
from alphaz.models.database.models import AlphaTable, AlphaColumn
from sqlalchemy.dialects.mysql import INTEGER, TEXT, TINYINT, DOUBLE, DATETIME, VARCHAR
from sqlalchemy.orm import relationship
from sqlalchemy.sql.schema import ForeignKey
from sqlalchemy.sql.sqltypes import DATE
from core import core

BIND = "yugioh_app"
DB = core.db


class Edition(DB.Model, AlphaTable):
    __bind_key__ = BIND
    __tablename__ = "edition"

    e_id = AlphaColumn(INTEGER, primary_key=True, nullable=False, autoincrement=True)
    e_code = AlphaColumn(VARCHAR(10), nullable=False)
    e_name = AlphaColumn(VARCHAR(100), nullable=False)
    e_number_of_cards = AlphaColumn(INTEGER(11), nullable=False)
    e_release_date = AlphaColumn(DATETIME, nullable=False)
    e_img_url = AlphaColumn(TEXT, nullable=False)
    e_tech_displayed = AlphaColumn(TINYINT, nullable=False)

    edition_category = relationship("EditionCategory")
    cards = relationship("Card")
    #cards.order_by="desc(Card.c_index_in_edition)"