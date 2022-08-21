from collections import defaultdict
from datetime import datetime
import typing
from sqlalchemy import select, true
from alphaz.models.database.operators import Operators
from models.databases.yugiscan import Card, CardTemplate, Edition, EditionCategory, UserCollection
from core import core
from models.dataclass.edition import EditionCollectionModel
from sqlalchemy import and_

from models.dataclass.user_collection import UserCollectionDataclass
from utils.yugiscan.card_function import select_card_by_id

DB = core.db
API = core.api


def select_user_collection(
    user_id: int,
    code_edition: str = None,
    code_card: str = None,
    name_edition: str = None,
    name_card: str = None,
    page: int = None,
    page_size: int = None,
) -> typing.List[EditionCollectionModel]:

    q  = select(
        EditionCategory,
        Edition
    ).outerjoin(
        EditionCategory, 
        (Edition.e_id == EditionCategory.ec_edition_id)
    )
    
    filter = None
    if code_edition is not None and name_edition is not None:
        filter = and_(Edition.e_code.like(code_edition.upper()), Edition.e_name.like(name_edition.upper()))
    elif code_edition is not None:
        filter = Edition.e_code.like(code_edition.upper())
    elif name_edition is not None:
        filter = Edition.e_code.like(name_edition.upper())
    
    if filter is not None:
        q = q.filter(filter)

    q =q.limit(page_size).offset(page * page_size).order_by(Edition.e_release_date).subquery()

    q = select(
        Card,
        CardTemplate,
        q
    ).outerjoin(
        Card, 
        (q.c.e_id == Card.c_edition_id)
    ).outerjoin(
        CardTemplate, 
        (Card.c_template_id == CardTemplate.ct_id)
    ).order_by(Card.c_index_in_edition)

    filter = None
    if code_card is not None and name_card is not None:
        filter = and_(Card.c_real_code.like(code_card.upper()), Card.c_name.like(name_card.upper()))
    elif code_card is not None:
        filter = Card.c_real_code.like(code_card.upper())
    elif name_card is not None:
        filter = Card.c_name.like(name_card.upper())
    
    if filter is not None:
        q = q.filter(filter)

    q = q.subquery()

    q = select(
        UserCollection,
        q
    ).outerjoin(
        UserCollection, 
        (and_(q.c.c_id == UserCollection.uc_card_id, UserCollection.uc_user_id == user_id))
    ).subquery()

    results = DB.select_query(
        query=DB.session.query(q),
        json=true
    )

    nested = []
    for item in results:
        edition_id_list = list(map(lambda x: x[Edition.e_id.key], nested))
        if len(nested) == 0 or item[Edition.e_id.key] not in edition_id_list:
            nested.append(
                {
                    Edition.e_id.key: item[Edition.e_id.key],
                    Edition.e_code.key: item[Edition.e_code.key],
                    Edition.e_name.key: item[Edition.e_name.key],
                    Edition.e_img_url.key: item[Edition.e_img_url.key],
                    Edition.e_release_date.key: item[Edition.e_release_date.key],
                    Edition.e_tech_displayed.key: item[Edition.e_tech_displayed.key],
                    Edition.cards.key: [],
                }
            )
            edition_id_list = list(map(lambda x: x[Edition.e_id.key], nested))

        nested[edition_id_list.index(item[Edition.e_id.key])][Edition.cards.key].append(
            {
                Card.c_id.key: item[Card.c_id.key],
                Card.c_name.key: item[Card.c_name.key],
                Card.c_code.key: item[Card.c_code.key],
                Card.c_attack.key: item[Card.c_attack.key],
                Card.c_defense.key: item[Card.c_defense.key],
                Card.c_attribute.key: item[Card.c_attribute.key],
                Card.c_level.key: item[Card.c_level.key],
                Card.c_img_url.key: item[Card.c_img_url.key],
                Card.c_index_in_edition.key: item[Card.c_index_in_edition.key],
                Card.c_quantity_in_edition.key: item[Card.c_quantity_in_edition.key],
                Card.c_rarity.key: item[Card.c_rarity.key],
                Card.c_real_code.key: item[Card.c_real_code.key],
                Card.c_text.key: item[Card.c_text.key],
                Card.c_type.key: item[Card.c_type.key],
                Card.template.key: {
                    CardTemplate.ct_id.key: item[CardTemplate.ct_id.key],
                    CardTemplate.ct_libelle.key: item[CardTemplate.ct_libelle.key],
                },
                Card.user_collection.key: {
                    UserCollection.uc_id.key: item[UserCollection.uc_id.key],
                    UserCollection.uc_card_id.key: item[UserCollection.uc_card_id.key],
                    UserCollection.uc_user_id.key: item[UserCollection.uc_user_id.key],
                    UserCollection.uc_date_insert.key: item[UserCollection.uc_date_insert.key],
                    UserCollection.uc_date_update.key: item[UserCollection.uc_date_update.key],
                    UserCollection.uc_number_of_copy.key: item[UserCollection.uc_number_of_copy.key],
                }
            }
        )

    return [EditionCollectionModel.map_from_dict(n, automap=True) for n in nested]

def select_card_in_user_collection(
    card_id: int,
    user_id: int
) -> UserCollectionDataclass:

    return next(
        iter(
            DB.select(
                model=UserCollection,
                dataclass=UserCollectionDataclass,
                filters=[
                    {
                        UserCollection.uc_user_id: user_id,
                        UserCollection.uc_card_id: card_id
                    }
                ]
            )
        ), None
    )


def insert_user_collection(
    card_id: int,
    nbr_copy: int,
    user_id: int,
) -> UserCollectionDataclass:

    username = API.get_logged_user()["username"]

    date_now = datetime.now()

    existing_card = select_card_by_id(card_id=card_id)

    if existing_card is None:
        return f"Card with id={card_id} not found in collection of {username}"

    existing_user_collection = select_card_in_user_collection(card_id=card_id, user_id=user_id)

    if existing_user_collection is not None:
        if existing_user_collection.nbr_copy == nbr_copy:
            return f"Card already existing in the collection of {username} with same number of copy"

        user_collection_updated = UserCollection(
            **{
                UserCollection.uc_id.key: existing_user_collection.id,
                UserCollection.uc_user_id.key: existing_user_collection.user_id,
                UserCollection.uc_card_id.key:existing_user_collection.card_id,
                UserCollection.uc_number_of_copy.key: nbr_copy,
                UserCollection.uc_date_insert.key: existing_user_collection.date_insert,
                UserCollection.uc_date_update.key: date_now
            }
        )

        DB.update(
            model=user_collection_updated,
        )

        return select_card_in_user_collection(card_id=card_id, user_id=user_id)

    new_user_collection = UserCollection(
            **{
                UserCollection.uc_user_id.key: user_id,
                UserCollection.uc_card_id.key:card_id,
                UserCollection.uc_number_of_copy.key: nbr_copy,
                UserCollection.uc_date_insert.key: date_now,
                UserCollection.uc_date_update.key: date_now
            }
        )

    DB.add(
        model=new_user_collection,
    )

    return select_card_in_user_collection(card_id=card_id, user_id=user_id)


def delete_user_collection(
    card_id: int,
    user_id: int
) -> UserCollectionDataclass:

    username = API.get_logged_user()["username"]

    existing_user_collection = select_card_in_user_collection(card_id=card_id, user_id=user_id)

    if existing_user_collection is None:
        
        return f"Card with id={card_id} not found in collection of {username}"

    
    DB.delete(
        model=UserCollection,
        filters=[
            {
                UserCollection.uc_card_id.key: card_id
            }
        ]
    )

    return f"Card with id={card_id} was succesffuly deleted from collection of {username}"