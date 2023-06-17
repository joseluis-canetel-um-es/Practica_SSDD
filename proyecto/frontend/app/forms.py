from flask_wtf import FlaskForm
from wtforms import (StringField, PasswordField, BooleanField, FileField, SubmitField)
from wtforms.validators import InputRequired, Length, Email, EqualTo

class LoginForm(FlaskForm):
    email = StringField('email', validators=[Email()])
    password = PasswordField('password', validators=[InputRequired()])
    remember_me = BooleanField('remember_me')

<<<<<<< HEAD
class SignupForm(FlaskForm):
    email = StringField('email', validators=[Email()])
    password = PasswordField('password', validators=[InputRequired()])
    name = StringField('name', validators=[InputRequired()])

=======
# clase definida para el formulario de registro
class RegistrationForm(FlaskForm):
    email = StringField('email', validators=[Email()])
    name = StringField('name', validators=[InputRequired()])
    password = PasswordField('password', validators=[InputRequired()])
    submit = SubmitField('Sign Up')
>>>>>>> branch 'main' of https://github.com/joseluis-canetel-um-es/Practica_SSDD.git
