pragma solidity ^0.4.11; // Solidity compiler verson to use

contract SmartReward {
    address public owner; // Store owner address
    mapping (address => uint) private reward; // var to store reward of each
												// user

    function SmartReward (uint initValue) {

        owner = msg.sender;
        reward[msg.sender] = initValue;
    }

    // modifier to check if caller is owner
    modifier ifOwner() { 
        if (msg.sender != owner) {
            revert();
        } 
        _; /* _ means contiune normal flow */
    }

    event BroadcastReward(address _sender, address _receiver, uint256 _value, string _event, string _reason);

// Show available reward points
    function showAvailReward() constant returns(uint) {
        return reward[msg.sender];
    }

// Show user's current reward points
    function showMyReward(address _address) constant returns(uint) {
        return reward[_address];
    }

// Give reward after completing certain task
    function giveReward(address _to, uint _value, string _rewardReasonMsg) ifOwner {
        if (reward[msg.sender] < _value ) revert(); // Check availability
        if (reward[_to] + _value < reward[_to]) revert(); // Check for
															// overflows
        reward[msg.sender] -= _value;
        reward[_to] += _value;

        BroadcastReward(msg.sender, _to, _value, "Got reward point(s).", _rewardReasonMsg);
        
    }

// Spend reward to buy items
    function spendReward(address _spender, uint _value, string _spentOnMsg) ifOwner {
        address _blank;
        if (reward[_spender] < _value ) revert(); // Check availability
        reward[_spender] -= _value;

        BroadcastReward(_spender, _blank, _value, "Spent reward point(s).", _spentOnMsg);  
    }

// Send own reward to other
    function sendReward(address _sender, address _receiver, uint _value, string _senderMsg) ifOwner {
        if (reward[_sender] < _value ) revert(); // Check availability
        if (reward[_receiver] + _value < reward[_receiver]) revert(); // Check
																		// for
																		// overflows

        reward[_sender] -= _value;
        reward[_receiver] += _value;

        BroadcastReward(_sender, _receiver, _value, "Sent reward point(s).", _senderMsg);
    }

    // Set total reamaining reward to distribute.
    function setAvailRewardPoints(uint _value) ifOwner {
        address _blank;
        if (reward[msg.sender] + _value < reward[msg.sender]) revert(); // Check
																		// for
																		// overflows
        reward[msg.sender] = _value;

        BroadcastReward(msg.sender, _blank, _value, "Set new avalilable reward point(s).", "");
    }

    // Fallback function - Called if other functions don't match call or
    function () {
        revert(); // revert() reverts state to before call
    }    
}
