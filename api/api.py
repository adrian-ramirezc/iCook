from flask import Flask
from flask_restx import Api, Resource, fields, reqparse

from data.database import db_session, init_db
from data.models.user import User
from data.repositories.users import UserRepository
from data.services.sign_up import SignUpService

app = Flask(__name__)
api = Api(app)

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


@app.teardown_appcontext
def shutdown_session(exception=None):
    db_session.remove()


sign_up_service = SignUpService(user_repository=UserRepository)


@api.route("/signup")
class HelloWorld(Resource):

    @api.expect(user_model)
    def post(self):
        user_dict = api.payload
        sign_up_service.add(user_dict=user_dict)
        return


if __name__ == "__main__":
    app.run(debug=True)
