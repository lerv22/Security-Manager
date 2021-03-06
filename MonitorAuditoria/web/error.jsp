<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8"/>
        <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
        <meta name="viewport" content="width=device-width, initial-scale=1"/>
        <link rel="icon" href="img/icon.png"/>
        <title>NARF Privacy Manager</title>
        <link href="css/bootstrap.css" rel="stylesheet"/>
        <link href="css/custom.css" rel="stylesheet"/>
        <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
        <!--[if lt IE 9]>
          <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
          <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
        <![endif]-->
    </head>
    <body style="background-color: #222">
        <br/><div class="container" style="border: 2px #000 solid">            
            <br/><div class="col-md-12" style="text-align: center; background-color: #d2322d; padding: 15px;margin: 0px; color: white">
                <h1>Connection Parameters</h1>
            </div>
            <div class="row">
                <div class="col-md-4">
                    <br/><br/><br/><img class="center-block" src="img/logo.png" alt="NARF Privacy Manager"/>
                </div>
                <h1 style="color: white">Connection Parameters</h1>
                <h3 style="color: red">The connection has been rejected. Check the login data as well as the parameters of the database and try again</h3>
                <h2 style="color: white">User Information</h2>
                <form action="Login" method="post">
                    <div class="col-md-8">                    
                        <h2 style="color: white">User Information</h2>
                        <div class="form-group">
                            <input type="text" class="form-control input-lg" placeholder="Username" name="Username" value="luis">
                        </div>
                        <div class="form-group">
                            <input type="password" class="form-control input-lg" placeholder="Password" name="Password" value="rontokbro">
                        </div>
                        <h2 style="color: white">Database Information</h2>
                        <div class='form-group'>
                            <input type='text' class='form-control input-lg' placeholder='SID' name='SID' value="XE">
                        </div>
                        <div class='form-group'>
                            <input type='text' class='form-control input-lg' placeholder='Host Name' name='HostName' value="localhost">
                        </div>
                        <div class='form-group'>
                            <input type='text' class='form-control input-lg' placeholder='Port' name='Port' value="1521">
                        </div><br/>
                    </div>
                    <div class="col-md-12">
                        <div class='form-group'>
                            <table style="width: 100%">
                                <tr>
                                    <td style="width: 50%"><button class='btn btn-primary btn-lg btn-block btn-danger' type='submit'>Log In</button></td>
                                    <td style="width: 50%"><button class='btn btn-primary btn-lg btn-block btn-default' type="reset">Clear</button></td>
                                </tr>
                            </table>                      
                        </div>
                    </div>
                </form>
            </div>
        </div>
        <script type="text/javascript" src="js/jquery-1.11.0.js"></script>
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
    </body>
</html>
