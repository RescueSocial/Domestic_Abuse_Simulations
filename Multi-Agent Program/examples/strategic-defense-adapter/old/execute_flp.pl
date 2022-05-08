:- consult('/var/lib/myfrdcsa/codebases/minor/free-life-planner/lib/util/util.pl').
:- consult('/var/lib/myfrdcsa/codebases/minor/formalog-pengines/formalog_pengines/formalog_pengines_client').
:- consult('/var/lib/myfrdcsa/codebases/minor/formalog-pengines/formalog_pengines/formalog_pengines_server').
:- consult('/var/lib/myfrdcsa/codebases/minor/formalog-pengines/attempts/pengines/1/scripts/formalog_pengines_call.pl').

jpl_test_execute_action(Agent,Action,Results) :-
	query_agent_bindings(flp,localhost,[Result],execute_action(Agent,Action,Result),[[Results]]),
	view([agent,Agent,action,Action,result,Result,results,Results]).
