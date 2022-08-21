import typing
from alphaz.models.main import AlphaDataclass, AlphaMappingAttribute, dataclass, Dict, field


from models.databases.yugiscan import UserCollection

@dataclass
class UserCollectionDataclass(AlphaDataclass):
    id: int = None
    user_id: int = None
    card_id: int = None
    nbr_copy: int = None
    date_insert: str = None
    date_update: str = None

    @classmethod
    def auto_map_from_dict(class_, dict_values: Dict[str, object]):
        return super().auto_map_from_dict(
            dict_values,
            {
                "id": UserCollection.uc_id.key,
                "user_id": UserCollection.uc_user_id.key,
                "card_id": UserCollection.uc_card_id.key,
                "nbr_copy": UserCollection.uc_number_of_copy.key,
                "date_insert": UserCollection.uc_date_insert.key,
                "date_update": UserCollection.uc_date_update.key,
            },
        )