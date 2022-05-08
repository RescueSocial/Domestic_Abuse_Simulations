% jpl_test_fac(+N, -F) :-
%   F is the factorial of the positive integer N

jpl_test_execute_action(Agent,Action,Result) :-
	(   (	Action = lock) -> Result = locked ;
	    (	(   Action = unlock) -> Result = unlocked ; true)).


