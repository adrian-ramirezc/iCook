import logging
from typing import List, Optional, Tuple

from sqlalchemy import delete, select, update

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
        stmt = select(Post).where(Post.username == username).order_by(Post.date.desc())
        result = self.db_session.execute(stmt)
        posts = result.scalars().all()
        if not posts:
            logger.debug(f"No posts found for User(username={username})")
        return posts

    def get_for_username(self, username: str) -> List[Post]:
        stmt = select(Post).where(Post.username != username).order_by(Post.date.desc())
        result = self.db_session.execute(stmt)
        posts = result.scalars().all()
        if not posts:
            logger.debug(f"No feed posts found for User(username={username})")
        return posts

    def delete(self, id: int) -> bool:
        success = False
        try:
            stmt = delete(Post).where(Post.id == id)
            self.db_session.execute(stmt)
            self.db_session.commit()
            success = True
            logger.debug(f"Post id = {id} deleted")
        except Exception as e:
            logger.error(stmt, e.args)
        return success

    def append_user_to_liked_by(self, id: int, username: str) -> bool:
        return self.update_liked_by(id=id, username=username, append=True)

    def pop_user_from_liked_by(self, id: int, username: str) -> bool:
        return self.update_liked_by(id=id, username=username, append=False)

    def update_liked_by(self, id: int, username: str, append: bool) -> bool:
        success = False

        get_counter_success, liked_by = self.get_liked_by(id=id)
        if not get_counter_success:
            return success

        if append:
            liked_by.append(username)
        else:
            liked_by.remove(username)
        try:
            stmt = update(Post).where(Post.id == id).values(liked_by=liked_by)
            self.db_session.execute(stmt)
            self.db_session.commit()
            success = True
        except Exception as e:
            logger.error(e)
        return success

    def get_liked_by(self, id) -> Tuple[bool, List[str]]:
        success = False
        try:
            stmt = select(Post).where(Post.id == id)
            result = self.db_session.execute(stmt)
            posts: List[Post] = result.scalars().all()
            if not posts:
                return success, 0
            post = posts[0]
            success = True
            liked_by = post.liked_by
        except Exception as e:
            logger.error(e)
        return success, liked_by
