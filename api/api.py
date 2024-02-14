from flask import Flask
from flask_restx import Api, Resource, fields, reqparse

from data.database import db_session, init_db
from data.models.user import User
from data.repositories.user_repository import UserRepository
from data.services.user_service import UserService

app = Flask(__name__)
api = Api(app, title="iCook")

init_db()

user_model = api.model(
    "User Model",
    {
        "name": fields.String,
        "lastname": fields.String,
        "username": fields.String,
        "password": fields.String,
        "description": fields.String,
        "picture": fields.String,
    },
)

user_repository = UserRepository(db_session=db_session)
user_service = UserService(user_repository=user_repository)


@app.teardown_appcontext
def shutdown_session(exception=None):
    db_session.remove()


@api.route("/signup")
class SignUp(Resource):
    @api.expect(user_model)
    def post(self):
        user_dict = api.payload
        user_service.add(user_dict=user_dict)
        return


if __name__ == "__main__":
    app.run(debug=True, host="0.0.0.0")
