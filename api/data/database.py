import os

from sqlalchemy import create_engine
from sqlalchemy.orm import declarative_base, scoped_session, sessionmaker
from sqlalchemy.orm.decl_api import DeclarativeMeta

from utils.imgs import encode_image_to_base64

engine = create_engine("postgresql://admin:secret@localhost/icook_db")
db_session = scoped_session(sessionmaker(autocommit=False, autoflush=False, bind=engine))
Base: DeclarativeMeta = declarative_base()


def clean_db():
    Base.metadata.drop_all(bind=engine)


def add_test_data():
    from data.models.post import Post
    from data.models.user import User

    # Users (password hashs = "qwe")
    users = [
        User(
            username="aramirez",
            name="Adrian",
            lastname="Ramirez",
            password="$2a$10$hJxu4TPm.wHAaJlkXv6h9.SK38nqZGRYoj0UdRWTKxfNyHf9A/d3K",
        ),
        User(
            username="fcastror",
            name="Franco",
            lastname="Castro",
            password="$2a$10$hJxu4TPm.wHAaJlkXv6h9.SK38nqZGRYoj0UdRWTKxfNyHf9A/d3K",
        ),
        User(
            username="atorres34",
            name="Angel",
            lastname="Torres",
            password="$2a$10$hJxu4TPm.wHAaJlkXv6h9.SK38nqZGRYoj0UdRWTKxfNyHf9A/d3K",
        ),
        User(
            username="aparedes12",
            name="Armando",
            lastname="Paredes",
            password="$2a$10$hJxu4TPm.wHAaJlkXv6h9.SK38nqZGRYoj0UdRWTKxfNyHf9A/d3K",
        ),
        User(
            username="equito66",
            name="Esteban",
            lastname="Quito",
            password="$2a$10$hJxu4TPm.wHAaJlkXv6h9.SK38nqZGRYoj0UdRWTKxfNyHf9A/d3K",
        ),
    ]

    pictures = []
    posts_folder_path = "data/imgs/posts"
    for filename in os.listdir(posts_folder_path):
        file_path = os.path.join(posts_folder_path, filename)
        if filename.endswith(".jpg"):
            pictures.append(encode_image_to_base64(file_path))

    posts = [
        Post(
            username="fcastror",
            description="Check out this amazing dish!",
            picture=pictures[0],
        ),
        Post(
            username="atorres34",
            description="Who would you share it with?",
            picture=pictures[1],
        ),
        Post(
            username="aparedes12",
            description="Best meal ever!",
            picture=pictures[2],
        ),
        Post(
            username="equito66",
            description="My first post!",
            picture=pictures[3],
        ),
        Post(
            username="aramirez",
            description="Nothing like local food!",
            picture=pictures[4],
        ),
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
