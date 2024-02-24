from typing import Optional

from sqlalchemy import Column, Integer, String

from data.database import Base


class User(Base):
    __tablename__ = "users"
    id = Column(Integer, primary_key=True)
    username = Column(String(20), unique=True)
    name = Column(String(50), nullable=False)
    lastname = Column(String(50), nullable=False)
    password = Column(String, nullable=False)
    description = Column(String(255), default="Hello, I am new to iCook!")
    picture = Column(String, default="")

    def __init__(
        self,
        username: str,
        name: str,
        lastname: str,
        password: str,
        description: Optional[str] = None,
        picture: Optional[str] = None,
    ):
        self.username = username
        self.name = name
        self.lastname = lastname
        self.password = password
        self.description = description
        self.picture = picture

    def __repr__(self):
        return f"User(username={self.username})"

    def from_dict(user_dict: dict):
        return User(**user_dict)
