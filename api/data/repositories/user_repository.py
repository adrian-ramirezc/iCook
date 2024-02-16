import logging
from typing import Optional

from sqlalchemy import select

from data.models.user import User

logger = logging.getLogger(__name__)


class UserRepository:
    def __init__(self, db_session):
        self.db_session = db_session

    def create(self, user: User) -> bool:
        success = False
        try:
            self.db_session.add(user)
            self.db_session.commit()
            success = True
            logger.debug(f"New {user} created")
        except Exception as e:
            logger.error(e)
        return success

    def get(self, username: str) -> Optional[User]:
        stmt = select(User).where(User.username == username)
        result = self.db_session.execute(stmt)
        users = result.scalars().all()
        if not users:
            logger.warning(f"User(username={username}) not found")
            return
        assert len(users) == 1, "usernames should be unique"
        return users[0]
