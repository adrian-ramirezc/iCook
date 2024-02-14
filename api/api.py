from flask import Flask
from flask_restx import Api, Resource

from data.database import db_session, init_db
from data.models.user import User
from data.repositories.users import Users

app = Flask(__name__)
api = Api(app)

init_db()


@app.teardown_appcontext
def shutdown_session(exception=None):
    db_session.remove()

@api.route('/add_user')
class HelloWorld(Resource):
    def get(self):
        user = User(
            username="aramirezc",
            name = "Adrian",
            last_name = "Ramirez",
            password="password"
            )
        Users.add(user)
        return {'user': "created"}

if __name__ == '__main__':
    app.run(debug=True)