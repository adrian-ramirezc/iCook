from data.database import db_session
from data.models.user import User


class UserRepository:
    def add(user: User):
        db_session.add(user)
        db_session.commit()
