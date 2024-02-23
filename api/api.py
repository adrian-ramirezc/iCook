import logging
from http import HTTPStatus

from flask import Flask
from flask_restx import Api, Resource, fields

from data.database import db_session, init_db
from data.repositories.post_repository import PostRepository
from data.repositories.user_repository import UserRepository
from data.services.post_service import PostService
from data.services.user_service import UserService

log_format = "%(asctime)s %(levelname)-8s %(filename)-30s %(message)s"
logging.basicConfig(format=log_format, level=logging.DEBUG)

app = Flask(__name__)
api = Api(app, title="iCook", default_label="iCook Namespace")

init_db()


user_model = api.model(
    "User Model",
    {
        "name": fields.String(example="Adrian"),
        "lastname": fields.String(example="Ramirez"),
        "username": fields.String(example="aramirez"),
        "password": fields.String(example="12345678"),
        "description": fields.String(example="Hello, my name is Adrian!"),
        "picture": fields.String(example="/path/to/picture"),
    },
)

post_model = api.model(
    "Post Model",
    {
        "username": fields.String(example="aramirez"),
        "description": fields.String(example="Hello, check out my new post!"),
        "picture": fields.String(example="/path/to/picture"),
    },
)
posts_model = api.model("Posts Model", {"posts": fields.List(fields.Nested(post_model))})

message_model = api.model(
    "Message",
    {
        "message": fields.String,
    },
)

user_repository = UserRepository(db_session=db_session)
user_service = UserService(user_repository=user_repository)

post_repository = PostRepository(db_session=db_session)
post_service = PostService(post_repository=post_repository)


@app.teardown_appcontext
def shutdown_session(exception=None):
    db_session.remove()


@api.route("/users/create")
class UsersCreate(Resource):
    @api.marshal_with(message_model)
    @api.expect(user_model)
    def post(self):
        user_dict = api.payload
        success = user_service.create(user_dict=user_dict)
        return (
            ({"message": "User created"}, HTTPStatus.OK)
            if success
            else ({"message": "User was not created"}, HTTPStatus.CONFLICT)
        )


@api.route("/users/update", methods=["PUT"])
class UsersCreate(Resource):
    @api.marshal_with(message_model)
    @api.expect(user_model)
    def put(self):
        user_dict = api.payload
        success = user_service.update(user_dict=user_dict)
        return (
            ({"message": "User created"}, HTTPStatus.OK)
            if success
            else ({"message": "User was not created"}, HTTPStatus.CONFLICT)
        )


@api.route("/users/<username>")
class UsersResource(Resource):
    @api.marshal_with(user_model, skip_none=True)
    def get(self, username):
        user = user_service.get(username=username)
        status_code = HTTPStatus.OK if user else HTTPStatus.NOT_FOUND
        return user, status_code


@api.route("/posts/create")
class PostsCreate(Resource):
    @api.marshal_with(message_model)
    @api.expect(post_model)
    def post(self):
        post_dict = api.payload
        success = post_service.create(post_dict=post_dict)
        return (
            ({"message": "Post created"}, HTTPStatus.OK)
            if success
            else ({"message": "Post was not created"}, HTTPStatus.CONFLICT)
        )


@api.route("/posts/<username>")
class PostsResource(Resource):
    @api.marshal_with(posts_model)
    def get(self, username):
        posts = post_service.get_by_username(username=username)
        status_code = HTTPStatus.OK if posts else HTTPStatus.NOT_FOUND
        return {"posts": posts}, status_code


if __name__ == "__main__":
    app.run(debug=True, host="0.0.0.0", use_reloader=False)
