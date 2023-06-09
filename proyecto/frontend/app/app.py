from hashlib import sha512
from flask import Flask, render_template, send_from_directory, url_for, request, redirect
from flask_login import LoginManager, login_manager, current_user, login_user, login_required, logout_user
import requests
import os

# Usuarios
from models import users, User

# Login
<<<<<<< HEAD
from forms import LoginForm
# Signup
from forms import SignupForm
=======
from forms import LoginForm, RegistrationForm

import hashlib

>>>>>>> branch 'main' of https://github.com/joseluis-canetel-um-es/Practica_SSDD.git

app = Flask(__name__, static_url_path='')
login_manager = LoginManager()
login_manager.init_app(app) # Para mantener la sesión

# Configurar el secret_key. OJO, no debe ir en un servidor git público.
# Python ofrece varias formas de almacenar esto de forma segura, que
# no cubriremos aquí.
app.config['SECRET_KEY'] = 'qH1vprMjavek52cv7Lmfe1FoCexrrV8egFnB21jHhkuOHm8hJUe1hwn7pKEZQ1fioUzDb3sWcNK1pJVVIhyrgvFiIrceXpKJBFIn_i9-LTLBCc4cqaI3gjJJHU6kxuT8bnC7Ng'

@app.route('/static/<path:path>')
def serve_static(path):
    return send_from_directory('static', path)

@app.route('/')
def index():
    return render_template('index.html')

"""
@app.route('/signup', methods=['GET', 'POST'])
def signup():
    if current_user.is_authenticated:
        return redirect(url_for('index'))
    
    if request.method == 'POST':
        # Obtener los datos del formulario
        name = request.form['name']
        email = request.form['email']
        password = request.form['password']
        
        # Validar los datos ingresados
        
        # Ejemplo de validación simple: verificar si el correo electrónico ya está registrado
        if users.isEmailRegister(email):
            error = 'El correo electrónico ya está registrado.'
            return render_template('signup.html', error=error)
        
        # Crear un nuevo usuario y agregarlo a tu base de datos
        new_user = User(name, email, password)
        users[email] = new_user
        
        # Redirigir a la página de inicio de sesión o a alguna otra página adecuada
        return redirect(url_for('login'))
    
    # Si la solicitud es un GET, renderizar la plantilla signup.html
    return render_template('signup.html')
"""

@app.route('/signup', methods=['GET', 'POST'])
def signup():
    if current_user.is_authenticated:
        return redirect(url_for('index'))
    else:
        error = None
        form = SignupForm(None if request.method != 'POST' else request.form)
        if request.method == "POST" and form.validate():

            email = form.email.data
            password = form.password.data
            name = form.name.data
            credenciales = {"email": email, "password": password, "name": name}

            error = 'Hola'
            """
            response = requests.post('http://backend-rest:8080/rest/checkLogin', json=credenciales)

            if response.status_code == 200: 
                user = User(int(response.json()['id']), response.json()['name'], form.email.data.encode('utf-8'), form.password.data.encode('utf-8'),response.json()['token'], int(response.json()['visits']))
                users.append(user)
                login_user(user, remember=form.remember_me.data)
                return redirect(url_for('profile'))
            else:
                error = 'Email o contraseña incorrectos'
            """
            
        return render_template('signup.html', form=form,  error=error)

@app.route('/login', methods=['GET', 'POST'])
def login():
    if current_user.is_authenticated:
        return redirect(url_for('index'))
    else:
        error = None
        form = LoginForm(None if request.method != 'POST' else request.form)
        if request.method == "POST" and form.validate():

            email = form.email.data
            password = form.password.data
            # se envia un hash del password
            credenciales = {"email": email, "password": hashlib.sha256( password.encode('utf-8')).hexdigest()}
            cabecera = {"Content-Type" : "application/json"}

            response = requests.post('http://localhost:8080/checkLogin', headers=cabecera,json=credenciales)

            if response.status_code == 200: 
                user = User(int(response.json()['id']), response.json()['name'], form.email.data.encode('utf-8'), form.password.data.encode('utf-8'),response.json()['token'], int(response.json()['visits']))
                users.append(user)
                login_user(user, remember=form.remember_me.data)
                return redirect(url_for('profile'))
            else:
                error = 'Email o contraseña incorrectos'

        return render_template('login.html', form=form,  error=error)
    

# nueva funcion para permitir registro de usuario -- KHOLOUD
@app.route('/signup', methods=['GET', 'POST'])
def signup():
    if current_user.is_authenticated:
        return redirect(url_for('index')) #si ya hay user 
    else:
        error = None
        form = RegistrationForm(None if request.method != 'POST' else request.form)
        if request.method == "POST" and form.validate():
            email = form.email.data
            name = form.name.data
            password = form.password.data

            cabecera = {"Content-Type" : "application/json"}
             # Aquí almacenar los datos del usuario en la base de datos
            # llamar a backend para peticion de almacenar
            # password modificado para tomar el hash
            credenciales_registro = {"email" : email,"name" : name,  "password" : hashlib.sha256( password.encode('utf-8')).hexdigest()}
            response = requests.post('http://localhost:8080/checkSignup', headers = cabecera, json=credenciales_registro)
            print("se ha hecho el request")
            if response.status_code == 200:
                # usuario ficticio
                #user = User(email, name, password)
                #users.append(user)

                #login_user(user)  # Opcional: Inicia sesión automáticamente después del registro

                #return redirect(url_for('index'))
                # redirige al usuario a la vista de login para poder iniciar sesión
                print("recibe 200 ok en registro")
                return redirect(url_for('login')) 
        else:
            error = 'Validación de registro incorrecta'
    return render_template('signup.html', form=form, error=error)

@app.route('/profile')
@login_required
def profile():
    return render_template('profile.html')

@app.route('/logout')
@login_required
def logout():
    logout_user()
    return redirect(url_for('index'))

@login_manager.user_loader
def load_user(user_id):
    for user in users:
        if user.id == int(user_id):
            return user
    return None

if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0')
