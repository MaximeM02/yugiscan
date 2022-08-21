from datetime import datetime
from alphaz.models.database.models import AlphaTable, AlphaColumn
from sqlalchemy.dialects.mysql import INTEGER, TEXT, TINYINT, DOUBLE, DATETIME, VARCHAR
from sqlalchemy.orm import relationship
from sqlalchemy.sql.schema import ForeignKey
from sqlalchemy.sql.sqltypes import DATE
from models.databases.edition import Edition
from core import DB

BIND = "YUGIOH"



class EditionCategory(DB.Model, AlphaTable):
    __bind_key__ = BIND
    __tablename__ = "edition_category"

    ec_id = AlphaColumn(INTEGER, primary_key=True, nullable=False, autoincrement=True)
    ec_name = AlphaColumn(VARCHAR(100), nullable=False)

    ec_edition_id = AlphaColumn(INTEGER, ForeignKey(Edition.e_id))

class CardTemplate(DB.Model, AlphaTable):
    __bind_key__ = BIND
    __tablename__ = "card_template"

    ct_id = AlphaColumn(INTEGER, primary_key=True, nullable=False, autoincrement=True)
    ct_libelle = AlphaColumn(VARCHAR(30), nullable=False)

class Card(DB.Model, AlphaTable):
    __bind_key__ = BIND
    __tablename__ = "card"

    c_id = AlphaColumn(INTEGER, primary_key=True, nullable=False, autoincrement=True)
    c_code = AlphaColumn(VARCHAR(20), nullable=False)
    c_name = AlphaColumn(VARCHAR(100), nullable=False)
    c_rarity = AlphaColumn(VARCHAR(50), nullable=False)
    c_attribute = AlphaColumn(VARCHAR(10), nullable=False)
    c_type = AlphaColumn(VARCHAR(50), nullable=False)
    c_level = AlphaColumn(VARCHAR(10), nullable=False)
    c_attack = AlphaColumn(VARCHAR(10), nullable=False)
    c_defense = AlphaColumn(VARCHAR(10), nullable=False)
    c_link_classification = AlphaColumn(VARCHAR(10), nullable=False)
    c_text = AlphaColumn(TEXT, nullable=False)
    c_img_url = AlphaColumn(VARCHAR(10), nullable=False)
    c_quantity_in_edition = AlphaColumn(INTEGER(11), nullable=False)
    c_real_code = AlphaColumn(VARCHAR(20), nullable=False)
    c_index_in_edition = AlphaColumn(INTEGER(50), nullable=False)

    c_edition_id = AlphaColumn(INTEGER(50), ForeignKey(Edition.e_id))

    c_template_id = AlphaColumn(INTEGER(50), ForeignKey(CardTemplate.ct_id))
    template = relationship("CardTemplate", lazy='joined')

    user_collection = relationship("UserCollection", lazy='joined')

class UserCollection(DB.Model, AlphaTable):
    __bind_key__ = BIND
    __tablename__ = "user_collection"

    uc_id = AlphaColumn(INTEGER, primary_key=True, nullable=False, autoincrement=True)
    uc_user_id = AlphaColumn(INTEGER(11), nullable=False)
    uc_number_of_copy = AlphaColumn(INTEGER(11), nullable=False)
    uc_date_insert = AlphaColumn(DATETIME, nullable=False, default = datetime.now())
    uc_date_update = AlphaColumn(DATETIME, nullable=False, default = datetime.now())

    uc_card_id = AlphaColumn(INTEGER(11), ForeignKey(Card.c_id))
