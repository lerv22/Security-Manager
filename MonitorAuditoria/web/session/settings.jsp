<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta charset="utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>NARF Privacy Manager</title>
        <link href="../css/bootstrap.css" rel="stylesheet" />
        <link href="../css/font-awesome.css" rel="stylesheet" />
        <link href="../css/custom.css" rel="stylesheet" />
        <link href="../js/dataTables/dataTables.bootstrap.css" rel="stylesheet" />
        <link rel="icon" type="image/png" href="../img/icon.png"/>
    </head>
    <body>
        <div id="wrapper">
            <nav class="navbar navbar-default navbar-cls-top " role="navigation" style="margin-bottom: 0">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".sidebar-collapse">
                        <span class="sr-only">Toggle navigation</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                    <a class="navbar-brand" href="lastEvents.jsp"> <i class="fa fa-umbrella"></i> NARF Suite</a> 
                </div>
                <div style="color: white;padding: 15px 50px 5px 50px;float: right;font-size: 16px;">
                    <form method="post" action="Logout"><button type="submit" class="btn btn-danger square-btn-adjust">Logout &nbsp; <i class="fa fa-sign-out"></i></button></form>
                </div>
            </nav><!-- /. NAV TOP -->
            <nav class="navbar-default navbar-side" role="navigation">
                <div class="sidebar-collapse">
                    <ul class="nav" id="main-menu">
                        <li class="text-center">
                            <img src="../img/logo.png" class="user-image img-responsive hidden-xs" alt=""/>
                        </li>
                        <li>
                            <a href="lastEvents.jsp"><i class="fa fa-calendar  fa-2x"></i>Last Events</a>
                        </li>
                        <li>
                            <a href="sensibility.jsp"><i class="fa fa-warning fa-2x"></i> Information Sensibility </a>
                        </li>
                        <li>
                            <a href="roles.jsp"><i class="fa fa-users fa-2x"></i> Roles</a>                            
                        </li>
                        <li>
                            <a href="users.jsp"><i class="fa fa-user fa-2x"></i> Users</a>                            
                        </li> 
                        <li>
                            <a href="log.jsp"><i class="fa fa-file-text fa-2x"></i> Manager's Log </a>
                        </li>
                        <li>
                            <a class="active-menu" href="settings.jsp"><i class="fa fa-cogs fa-2x"></i> Settings </a>
                        </li>
                        <li>
                            <a href="licenses.jsp"><i class="fa fa-flag fa-2x"></i> Licenses </a>
                        </li>
                    </ul>
                </div>
            </nav> 
            <!-- /. NAV SIDE -->
            <div id="page-wrapper" >
                <div id="page-inner">
                    <div class="row">
                        <div class="col-md-12">
                            <h2>Settings</h2> 
                        </div>
                    </div><!-- /. ROW -->
                    <hr/>
                    <p>You must provide a User for access audit, in audit by <strong>Session</strong> or <strong>All</strong> the user is optional</p>
                    <p>Click <strong>Change Audit Method </strong> to apply the changes</p>
                    <p>After enable or disable audit be patient, wait at least one minute according to use the software again</p>
                    <hr/>                    
                    <form method="post" action="#">
                        <div id="space" style="display: none">
                            <h4>Audit Parameters</h4>
                            <div class="form-group">
                                <select class="input-lg" id="selected">  
                                    <option disabled="disabled" value="" selected="selected">Select a type</option>
                                    <option value="2">All</option>
                                    <option value="1">Session</option>
                                    <option value="3">Access</option>
                                </select>
                                <input class="input-lg" name="user" id="user" type="text" placeholder="username"/>
                                <button id="change" name="change" class="btn btn-primary btn-lg" disabled="disabled">Change Audit Method</button>
                            </div><hr/>
                        </div>
                        <div class="input-group">
                            <button class="btn btn-danger btn-lg" name="suspend" id="suspend" type= "submit" style="display: none">Suspend Audit</button>
                            <button class=" btn btn-success btn-lg" name="enable" id="enable" type= "submit" style="display: none">Enable Audit</button>
                        </div>
                    </form>
                </div><!-- /. PAGE INNER -->
            </div><!-- /. PAGE WRAPPER -->
        </div><!-- /. WRAPPER -->
        <script src="../js/jquery-1.10.2.js"></script>
        <script src="../js/bootstrap.min.js"></script>
        <script src="../js/jquery.metisMenu.js"></script>
        <script src="../js/custom.js"></script>
        <script src="../js/settings.js"></script>
    </body>
</html>
