from typing import Dict, Optional

from data.models.user import User
from data.repositories.user_repository import UserRepository


class UserService:
    def __init__(self, user_repository: UserRepository):
        self.user_repository = user_repository

    def create(self, user_dict: dict) -> bool:
        user = User.from_dict(user_dict)
        return self.user_repository.create(user=user)

    def get(self, username: str) -> Optional[User]:
        return self.user_repository.get(username=username)

    def update(self, user_dict: Dict[str, str]):
        username = user_dict["username"]
        user_dict.pop("username")
        return self.user_repository.update(username=username, keys=user_dict)
