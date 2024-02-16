import logging
from typing import List, Optional

from sqlalchemy import select

from data.models.post import Post
from data.models.user import User

logger = logging.getLogger(__name__)


class PostRepository:
    def __init__(self, db_session):
        self.db_session = db_session

    def create(self, post: Post) -> bool:
        success = False
        try:
            self.db_session.add(post)
            self.db_session.commit()
            success = True
            logger.debug(f"New {post} created")
        except Exception as e:
            logger.error(e)
        return success

    def get_by_username(self, username: str) -> List[Post]:
        stmt = select(Post).where(Post.username == username)
        result = self.db_session.execute(stmt)
        posts = result.scalars().all()
        if not posts:
            logger.debug(f"No posts found for User(username={username})")
        return posts