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
    user_pictures = []
    users_folder_path = "data/imgs/users"
    for filename in os.listdir(users_folder_path):
        file_path = os.path.join(users_folder_path, filename)
        if filename.endswith(".jpg"):
            user_pictures.append(encode_image_to_base64(file_path))

    users = [
        User(
            username="aramirez",
            name="Adrian",
            lastname="Ramirez",
            password="$2a$10$hJxu4TPm.wHAaJlkXv6h9.SK38nqZGRYoj0UdRWTKxfNyHf9A/d3K",
            picture=user_pictures[0],
        ),
        User(
            username="fcastror",
            name="Franco",
            lastname="Castro",
            password="$2a$10$hJxu4TPm.wHAaJlkXv6h9.SK38nqZGRYoj0UdRWTKxfNyHf9A/d3K",
            picture=user_pictures[3],
        ),
        User(
            username="atorres34",
            name="Angel",
            lastname="Torres",
            password="$2a$10$hJxu4TPm.wHAaJlkXv6h9.SK38nqZGRYoj0UdRWTKxfNyHf9A/d3K",
            picture=user_pictures[1],
        ),
        User(
            username="aparedes12",
            name="Armando",
            lastname="Paredes",
            password="$2a$10$hJxu4TPm.wHAaJlkXv6h9.SK38nqZGRYoj0UdRWTKxfNyHf9A/d3K",
            picture=user_pictures[2],
        ),
        User(
            username="equito66",
            name="Esteban",
            lastname="Quito",
            password="$2a$10$hJxu4TPm.wHAaJlkXv6h9.SK38nqZGRYoj0UdRWTKxfNyHf9A/d3K",
            picture=user_pictures[4],
        ),
    ]

    post_pictures = []
    posts_folder_path = "data/imgs/posts"
    for filename in os.listdir(posts_folder_path):
        file_path = os.path.join(posts_folder_path, filename)
        if filename.endswith(".jpg"):
            post_pictures.append(encode_image_to_base64(file_path))

    posts = [
        Post(
            username="fcastror",
            description="Check out this amazing dish!",
            picture=post_pictures[0],
        ),
        Post(
            username="atorres34",
            description="Who would you share it with?",
            picture=post_pictures[1],
        ),
        Post(
            username="aparedes12",
            description="Best meal ever!",
            picture=post_pictures[2],
        ),
        Post(
            username="equito66",
            description="My first post!",
            picture=post_pictures[3],
        ),
        Post(
            username="aramirez",
            description="Nothing like local food!",
            picture=post_pictures[4],
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
