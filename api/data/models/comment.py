from datetime import datetime
from typing import Optional

from sqlalchemy import Column, DateTime, ForeignKey, Integer, String

from data.database import Base


class Comment(Base):
    __tablename__ = "comments"
    id = Column(Integer, primary_key=True)
    username = Column(String, ForeignKey("users.username", ondelete="cascade"), nullable=False)
    post_id = Column(Integer, ForeignKey("posts.id", ondelete="cascade"), nullable=False)
    text = Column(String, nullable=False)
    date = Column(DateTime)

    def __init__(
        self,
        username: str,
        post_id: str,
        text: str,
        date: Optional[datetime] = None,
        id: Optional[int] = None,
    ):
        if date is None:
            self.date = datetime.now()
        self.id = id
        self.username = username
        self.post_id = post_id
        self.text = text

    def __repr__(self):
        return f"Comment(username={self.username}, post_id={self.post_id}, text={self.text})"

    def from_dict(user_dict: dict):
        return Comment(**user_dict)
