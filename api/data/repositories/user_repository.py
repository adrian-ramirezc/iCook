from data.models.user import User


class UserRepository:
    def __init__(self, db_session):
        self.db_session = db_session

    def add(self, user: User):
        self.db_session.add(user)
        self.db_session.commit()
