import logging
from typing import Dict, List, Optional

from sqlalchemy import select, update

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
            logger.error(e.args)
        return success

    def get(self, username: str) -> Optional[User]:
        stmt = select(User).where(User.username == username)
        result = self.db_session.execute(stmt)
        users = result.scalars().all()
        if not users:
            logger.warning(f"User(username={username}) not found, request: {stmt}")
            return
        assert len(users) == 1, "usernames should be unique"
        return users[0]

    def log_in(self, username: str, password: str) -> str:
        retrieved_user = self.get(username=username)
        if not retrieved_user:
            return "User not found"
        if not retrieved_user.verify_password(password=password):
            return "Wrong password"
        return "success"

    def get_many(self, usernames: List[str]) -> List[User]:
        stmt = select(User).where(User.username.in_(usernames))
        result = self.db_session.execute(stmt)
        users = result.scalars().all()
        return users

    def update(self, username: str, keys: Dict[str, str]):
        success = False
        try:
            stmt = update(User).where(User.username == username).values(**keys)
            self.db_session.execute(stmt)
            self.db_session.commit()
            success = True
            logger.debug(f"User {username} updated")
        except Exception as e:
            logger.error(stmt, e.args)
        return success
