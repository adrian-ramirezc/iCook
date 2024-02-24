from datetime import datetime
from typing import Optional

from sqlalchemy import Column, DateTime, Integer, String

from data.database import Base


class Post(Base):
    __tablename__ = "posts"
    id = Column(Integer, primary_key=True)
    username = Column(String(20), nullable=False)
    description = Column(String(255), nullable=False)
    picture = Column(String)
    date = Column(DateTime)

    def __init__(
        self,
        username: str,
        description: str,
        date: Optional[datetime] = None,
        picture: Optional[str] = None,
        id: Optional[int] = None,
    ):
        if date is None:
            self.date = datetime.now()
        self.id = id
        self.username = username
        self.description = description
        self.picture = picture

    def __repr__(self):
        return f"Post(author={self.username}, description={self.description})"

    def from_dict(user_dict: dict):
        return Post(**user_dict)
