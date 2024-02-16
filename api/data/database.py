from sqlalchemy import create_engine
from sqlalchemy.orm import declarative_base, scoped_session, sessionmaker
from sqlalchemy.orm.decl_api import DeclarativeMeta

engine = create_engine("postgresql://admin:secret@localhost/icook_db")
db_session = scoped_session(sessionmaker(autocommit=False, autoflush=False, bind=engine))
Base: DeclarativeMeta = declarative_base()


def clean_db():
    Base.metadata.drop_all(bind=engine)


def add_test_data():
    from data.models.post import Post
    from data.models.user import User

    # Users
    users = [
        User(username="aramirez", name="Adrian", lastname="Ramirez", password="test"),
        User(username="user1", name="User1", lastname="UserLastName1", password="test"),
        User(username="user2", name="User2", lastname="UserLastName2", password="test"),
        User(username="user3", name="User3", lastname="UserLastName3", password="test"),
        User(username="user4", name="Uder4", lastname="UserLastName4", password="test"),
    ]

    posts = [
        Post(username="aramirez", description="Post 1"),
        Post(username="aramirez", description="Post 2"),
        Post(username="user2", description="Post of user 2"),
        Post(username="user3", description="Post of usere 3"),
        Post(username="user4", description="Post of user 4"),
    ]

    for user in users:
        db_session.add(user)

    for post in posts:
        db_session.add(post)

    db_session.commit()


def init_db():
    # import all modules here that might define models so that
    # they will be registered properly on the metadata.  Otherwise
    # you will have to import them first before calling init_db()
    import data.models

    clean_db()  # For dev/testing
    Base.metadata.create_all(bind=engine)
    add_test_data()  # For dev/testing
    print("All set up!")
