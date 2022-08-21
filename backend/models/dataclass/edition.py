import datetime
from platform import release
import typing
from alphaz.models.main import AlphaDataclass, dataclass, Dict, field


from models.databases.yugiscan import Edition, EditionCategory
from models.dataclass.card import CardCollectionModel, CardModel

@dataclass
class EditionCategoryModel(AlphaDataclass):
    id: int = None
    name: str = None

    @classmethod
    def auto_map_from_dict(class_, dict_values: Dict[str, object]):
        return super().auto_map_from_dict(
            dict_values,
            {
                "id": EditionCategory.ec_id.key,
                "name": EditionCategory.ec_name.key,
            },
        )

@dataclass
class EditionModel(AlphaDataclass):
    id: int = None
    code: str = None
    name: str = None
    img_url: str = None
    tech_displayed: bool = None
    category: EditionCategoryModel = None
    release_date: datetime.date = None
    cards: typing.List[CardModel] = field(default_factory=lambda: []) 

    @classmethod
    def auto_map_from_dict(class_, dict_values: Dict[str, object]):
        instance = super().auto_map_from_dict(
            dict_values,
            {
                "id": Edition.e_id.key,
                "code": Edition.e_code.key,
                "name": Edition.e_name.key,
                "img_url": Edition.e_img_url.key,
                "tech_displayed": Edition.e_tech_displayed.key,
                "category": Edition.edition_category.key,
                "release_date": Edition.e_release_date.key,
                "cards": Edition.cards.key,
            }
        )
        instance.process_edition_category(dict_values)

        return instance

    def process_edition_category(self, dict_values: Dict[str, object]):
        self.category = EditionCategoryModel.auto_map_from_dict(next(iter(dict_values[Edition.edition_category.key]), None))
        #self.cards = sorted(self.cards, key=lambda card: card.index)

@dataclass
class EditionCollectionModel(AlphaDataclass):
    id: int = None
    code: str = None
    name: str = None
    img_url: str = None
    tech_displayed: bool = None
    category: EditionCategoryModel = None
    release_date: datetime.date = None
    cards: typing.List[CardCollectionModel] = field(default_factory=lambda: []) 

    completion: float = None
    completion_full: float = None


    @classmethod
    def auto_map_from_dict(class_, dict_values: Dict[str, object]):
        instance = super().auto_map_from_dict(
            dict_values,
            {
                "id": Edition.e_id.key,
                "code": Edition.e_code.key,
                "name": Edition.e_name.key,
                "img_url": Edition.e_img_url.key,
                "tech_displayed": Edition.e_tech_displayed.key,
                "category": Edition.edition_category.key,
                "release_date": Edition.e_release_date.key,
                "cards": Edition.cards.key,
            },
            no_match=["completion", "completion_full"]
        )

        return instance

    def __post_init__(self):
        if self.id is None:
            return

        self.release_date = self.release_date.strftime("%Y-%m-%d")
        
        cu = [card.user_collection.nbr_copy if card.user_collection.nbr_copy<=card.quantity_in_edition else card.quantity_in_edition for card in self.cards if card.user_collection is not None]
        cf =  sum([card.quantity_in_edition for card in self.cards])

        self.completion_full = round(sum(cu) / cf * 100, 2) if cf > 0 else None
        self.completion = round(len(cu) / len(self.cards) * 100, 2) if cf > 0 else None