from datetime import datetime
from typing import List, Optional

from sqlalchemy import ARRAY, Column, DateTime, Integer, String

from data.database import Base


class Post(Base):
    __tablename__ = "posts"
    id = Column(Integer, primary_key=True)
    username = Column(String(20), nullable=False)
    description = Column(String(255), nullable=False)
    picture = Column(String)
    date = Column(DateTime)
    liked_by = Column(ARRAY(String), nullable=False)

    def __init__(
        self,
        username: str,
        description: str,
        date: Optional[datetime] = None,
        picture: Optional[str] = None,
        id: Optional[int] = None,
        liked_by: List[str] = [],
    ):
        if not date:
            self.date = datetime.now()
        else:
            self.date = self.date

        self.id = id
        self.username = username
        self.description = description
        self.picture = picture

        self.liked_by = liked_by

    def __repr__(self):
        return f"Post(author={self.username}, description={self.description})"

    def from_dict(user_dict: dict):
        return Post(**user_dict)
