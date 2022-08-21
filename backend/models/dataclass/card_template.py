import typing
from alphaz.models.main import AlphaDataclass, dataclass, Dict


from models.databases.yugiscan import Card, CardTemplate
from models.dataclass.user_collection import UserCollectionDataclass

@dataclass
class CardTemplateModel(AlphaDataclass):
    id: int = None
    name: str = None

    @classmethod
    def auto_map_from_dict(class_, dict_values: Dict[str, object]):
        return super().auto_map_from_dict(
            dict_values,
            {
                "id": CardTemplate.ct_id.key,
                "name": CardTemplate.ct_libelle.key,
            }
        )