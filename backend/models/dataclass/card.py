import typing
from alphaz.models.main import AlphaDataclass, dataclass, Dict


from models.databases.yugiscan import Card
from models.dataclass.card_template import CardTemplateModel
from models.dataclass.user_collection import UserCollectionDataclass

@dataclass
class CardModel(AlphaDataclass):
    id: int = None
    code: str = None
    real_code: str = None
    name: str = None
    rarity: int = None
    attribute: str = None
    type: str = None
    level: str = None
    attack: str = None
    defense:str = None
    link_classification: str = None
    text: str = None
    img_url: str = None
    quantity_in_edition: int = None
    index: int = None
    template: CardTemplateModel = None

    @classmethod
    def auto_map_from_dict(class_, dict_values: Dict[str, object]):
        return super().auto_map_from_dict(
            dict_values,
            {
                "id": Card.c_id.key,
                "real_code": Card.c_real_code.key,
                "code": Card.c_code.key,
                "name": Card.c_name.key,
                "rarity": Card.c_rarity.key,
                "attribute": Card.c_attribute.key,
                "type": Card.c_type.key,
                "level": Card.c_level.key,
                "attack": Card.c_attack.key,
                "defense": Card.c_defense.key,
                "link_classification": Card.c_link_classification.key,
                "text": Card.c_text.key,
                "img_url": Card.c_img_url.key,
                "quantity_in_edition": Card.c_quantity_in_edition.key,
                "index": Card.c_index_in_edition.key,
                "template": Card.template.key
            }
        )


@dataclass
class CardCollectionModel(AlphaDataclass):
    id: int = None
    code: str = None
    real_code: str = None
    name: str = None
    rarity: int = None
    attribute: str = None
    type: str = None
    level: str = None
    attack: str = None
    defense:str = None
    link_classification: str = None
    text: str = None
    img_url: str = None
    quantity_in_edition: int = None
    index: int = None
    template: CardTemplateModel = None
    user_collection: UserCollectionDataclass = None

    @classmethod
    def auto_map_from_dict(class_, dict_values: Dict[str, object]):
        return super().auto_map_from_dict(
            dict_values,
            {
                "id": Card.c_id.key,
                "real_code": Card.c_real_code.key,
                "code": Card.c_code.key,
                "name": Card.c_name.key,
                "rarity": Card.c_rarity.key,
                "attribute": Card.c_attribute.key,
                "type": Card.c_type.key,
                "level": Card.c_level.key,
                "attack": Card.c_attack.key,
                "defense": Card.c_defense.key,
                "link_classification": Card.c_link_classification.key,
                "text": Card.c_text.key,
                "img_url": Card.c_img_url.key,
                "quantity_in_edition": Card.c_quantity_in_edition.key,
                "index": Card.c_index_in_edition.key,
                "template": Card.template.key
            }
        )

    def __post_init__(self):

        if self.user_collection and self.user_collection.card_id is None:
            self.user_collection = None


