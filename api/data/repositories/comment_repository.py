import logging
from typing import List, Optional

from sqlalchemy import delete, select

from data.models.comment import Comment
from data.models.post import Post
from data.models.user import User

logger = logging.getLogger(__name__)


class CommentRepository:
    def __init__(self, db_session):
        self.db_session = db_session

    def create(self, comment: Comment) -> bool:
        success = False
        try:
            self.db_session.add(comment)
            self.db_session.commit()
            success = True
            logger.debug(f"New {comment} created")
        except Exception as e:
            logger.error(e)
        return success

    def get_by_post_id(self, post_id: int) -> List[Comment]:
        stmt = select(Comment).where(Comment.post_id == post_id).order_by(Comment.date.desc())
        result = self.db_session.execute(stmt)
        comments = result.scalars().all()
        if not comments:
            logger.debug(f"No comments found for Post(id={post_id})")
        return comments
