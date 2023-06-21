from hashlib import sha512
from flask import Flask, render_template, send_from_directory, url_for, request, redirect
from flask_login import LoginManager, login_manager, current_user, login_user, login_required, logout_user
import requests
import os

# Usuarios
from models import users, User

# Login
from forms import LoginForm, RegistrationForm, DatabaseForm

import hashlib
import logging


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

            response = requests.post('http://backend-rest:8080/Service/checkLogin', headers=cabecera,json=credenciales)

            if response.status_code == 200: 
                user = User(response.json()['id'], response.json()['name'], form.email.data.encode('utf-8'), form.password.data.encode('utf-8'),response.json()['token'], int(response.json()['visits']))
                users.append(user)
                login_user(user, remember=form.remember_me.data)
                return redirect(url_for('profile'))
            else:
                error = 'Email o contraseña incorrectos'

        return render_template('login.html', form=form,  error=error)
    

# nueva funcion para permitir registro de usuario -- KHOLOUD
@app.route('/signup', methods=['GET', 'POST'])
def signup():
    logging.basicConfig(level=logging.DEBUG)
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
            # llamar a backend para peticion de almacenar
            # password modificado para tomar el hash
            credenciales_registro = {"email" : email, "name" : name, "password" : hashlib.sha256( password.encode('utf-8')).hexdigest()}
            # response . text contiene el texto ( datos ) de la respuesta
            response = requests.post('http://backend-rest:8080/Service/checkSignup', headers = cabecera, json=credenciales_registro)
            if response.status_code == 200:
                return redirect(url_for('login')) 
            elif response.status_code == 409:
                error = "Email ya registrado"
            else:
                error = 'Validación de registro incorrecta'
    return render_template('signup.html', form=form, error=error)


@app.route('/profile')
@login_required
def profile():
    return render_template('profile.html')

@app.route('/createDatabases', methods=['GET', 'POST'])
@login_required
def createDatabases():
    error = None
    form = DatabaseForm(None if request.method != 'POST' else request.form)
    if request.method == "POST" and form.validate():
        id = current_user.id
        name = form.name.data
        key = form.key.data
        value = form.value.data
        key_convertido = None
        try:
            # Intentar convertir a entero
            key_convertido = int(key)
        except ValueError:
            try:
                # Intentar convertir a número en coma flotante
                key_convertido = float(key)
            except ValueError:
                # Valor no numérico, se considera una cadena de caracteres
                key_convertido = str(key)
        valor_convertido = None
        try:
            # Intentar convertir a entero
            valor_convertido = int(value)
        except ValueError:
            try:
                # Intentar convertir a número en coma flotante
                valor_convertido = float(value)
            except ValueError:
                # Valor no numérico, se considera una cadena de caracteres
                valor_convertido = str(value)

        cabecera = {"Content-Type" : "application/json"}
        # llamar a backend para peticion de almacenar
        # password modificado para tomar el hash
        datos_database = {"name" : name, "key" : key_convertido, "value":valor_convertido}
        # response . text contiene el texto ( datos ) de la respuesta
        response = requests.post('http://backend-rest:8080/Service/u/'+id+'/db', headers = cabecera, json=datos_database)
        if response.status_code == 201:
            error =  "Database registrada correctamente : "+str(response.headers.get('Location'))
        elif response.status_code == 400:
            error = 'No se ha podido crear la base de datos'
        else:
            error = 'Error no controlado'
    return render_template('createDatabases.html', form=form, error=error)

@app.route('/viewDatabases', methods=['GET'])
@login_required
def viewDatabases():
    # funcion para mostrar todas las bases de datos de un usuario (muestra los nombres o links)
    id = current_user.id
    logging.info("MI ID ES "+id)
    try:
        response = requests.get('http://backend-rest:8080/Service/u/'+id+'/dbinfo')
    except:
        error = 'No se ha podido hacer la conexion'
    
    if response.status_code == 200:
        databases = response.json()
        return render_template('viewDatabases.html', databases = databases)

    elif response.status_code == 204:
        databases = []
        return render_template('viewDatabases.html', databases = databases)
    else:
        error = 'No se ha podido obtener las bases de datos'
    return render_template('viewDatabases.html', error = error)    



@app.route('/database/<database_name>', methods=['GET'])
def database_info(database_name):
    # obtener la información de UNA base de datos con el ID proporcionado
    try:
        # @Path("/{id}/db/{name}")
        id = current_user.id
        response = requests.get('http://backend-rest:8080/Service/u/'+id+'/db/'+database_name)
    except:
        error = "No se ha podido hacer la conexion"

    if response.status_code == 200:

        database = response.json()
        return render_template('databaseInfo.html', database = database)
   
    else:
        error = 'No se ha recibido la base de datos'
        return render_template('base.html', error = error)
   # return render_template('databaseInfo.html', database_name=database_name)


@app.route('/logout')
@login_required
def logout():
    logout_user()
    return redirect(url_for('index'))

@login_manager.user_loader
def load_user(user_id):
    for user in users:
        #if user.id == int(user_id):
        if user.id == user_id:
            return user
    return None

if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0')