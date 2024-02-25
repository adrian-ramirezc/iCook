from typing import List

from data.models.comment import Comment
from data.repositories.comment_repository import CommentRepository


class CommentService:
    def __init__(self, comment_repository: CommentRepository):
        self.post_repository = comment_repository

    def create(self, comment_dict: dict) -> bool:
        comment = Comment.from_dict(comment_dict)
        return self.post_repository.create(comment=comment)

    def get_by_post_id(self, post_id: int) -> List[Comment]:
        return self.post_repository.get_by_post_id(post_id=post_id)
