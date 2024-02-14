from sqlalchemy import Column, Integer, String

from data.database import Base


class User(Base):
    __tablename__ = 'users'
    id = Column(Integer, primary_key=True)
    username = Column(String(15), unique = True)
    name = Column(String(50))
    last_name = Column(String(50))
    password = Column(String(50))

    def __init__(
            self,
            username=None,
            name = None,
            last_name = None,
            password = None
        ):
        self.username = username
        self.name = name,
        self.last_name = last_name
        self.password = password

    def __repr__(self):
        return f'<User {self.username!r}>'