import random
import string
from faker import Faker

fake = Faker("zh_CN")
teacher_ids = []
paper_ids = []
project_ids = []
course_ids = []

def generate_teacher_id():
    x = "".join(random.choices(string.digits, k=5))
    if x in teacher_ids: return generate_teacher_id()
    teacher_ids.append(x)
    return x

def generate_paper_id():
    x = random.randrange(int(1e9))
    if x in paper_ids: return generate_paper_id()
    paper_ids.append(x)
    return x

def generate_project_id():
    x = random.choice(string.ascii_uppercase) + "".join(random.choices(string.digits, k=7))
    if x in project_ids: return generate_project_id()
    project_ids.append(x)
    return x

def generate_course_id():
    x = "".join(random.choices(string.ascii_uppercase, k=2)) + "".join(random.choices(string.digits, k=6))
    if x in course_ids: return generate_course_id()
    course_ids.append(x)
    return x

with open("data.sql", "w") as f:
    for _ in range(5):
        id = generate_teacher_id()
        name = fake.name()
        gender = random.randint(1, 2)
        title = random.randint(1, 11)
        f.write(f"INSERT INTO Teachers (id, name, gender, title) VALUE('{id}', '{name}', {gender}, {title});\n")
    f.write("\n")

    for _ in range(5):
        id = generate_paper_id()
        name = fake.bs()
        source = random.choice(["SCI", "EI", "CSCD", "CSSCI"])
        year = random.randint(2010, 2020)
        typ = random.randint(1, 4)
        level = random.randint(1, 6)
        f.write(f"INSERT INTO Papers (id, name, source, year, type, level) VALUE('{id}', '{name}', '{source}', {year}, {typ}, {level});\n")
    f.write("\n")

    for _ in range(5):
        id = generate_project_id()
        name = fake.catch_phrase()
        source = random.choice(["NSF", "863", "973", "EU", "UNDP"])
        typ = random.randint(1, 5)
        fund = random.randint(5000, 10000) / 100.0
        start_year = random.randint(2010, 2015)
        end_year = random.randint(2016, 2020)
        f.write(f"INSERT INTO Projects (id, name, source, type, fund, start_year, end_year) VALUE('{id}', '{name}', '{source}', {typ}, {fund}, {start_year}, {end_year});\n")
    f.write("\n")

    for _ in range(5):
        id = generate_course_id()
        name = fake.sentence(4)[:-1]
        credit = random.randint(1, 6) * 10
        typ = random.randint(1, 2)
        f.write(f"INSERT INTO Courses (id, name, credit, type) VALUE('{id}', '{name}', {credit}, {typ});\n")
    f.write("\n")
