const Login = () => {

    return (
        <div>
            <h1>Accedi</h1>
            <form>
                <div>
                    <label>Nome utente o username</label>
                    <input type="text" placeholder="Inserisci nome utente o email" />
                </div>

                <div>
                    <label>Password</label>
                    <input type="password" placeholder="Inserisci una password" />
                </div>

                <div>
                    <input type="button" value="Accedi ora" />
                </div>
            </form>
        </div>
    )
}

export default Login;