from data.models.user import User
from data.repositories.user_repository import UserRepository


class UserService:
    def __init__(self, user_repository: UserRepository):
        self.user_repository = user_repository

    def add(self, user_dict: dict):
        user = User.from_dict(user_dict)
        self.user_repository.add(user=user)
