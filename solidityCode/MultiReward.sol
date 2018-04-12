pragma solidity ^0.4.11; // Solidity compiler version to use

contract SmartMultiReward {
    address public owner; // Store owner address
    mapping (address => mapping (bytes32 => uint)) reward;
    mapping (address => bytes32[]) availRewardTypes;
    // mapping (address => uint) private reward; // var to store reward of each
    //                                             // user

    function SmartMultiReward (uint initValue, bytes32 rewardType) {

        owner = msg.sender;
        reward[msg.sender][rewardType] = initValue;
        availRewardTypes[msg.sender].push(rewardType);

    }

    // modifier to check if caller is owner
    modifier ifOwner() { 
        if (msg.sender != owner) {
            revert();
        } 
        _; /*  _means contiune normal flow */
    }

    event BroadcastReward(address sender, address receiver, uint256 value, bytes32 rewardType, string rewardEvent, string reason);

    // Show available reward points
    function showAvailReward(bytes32 rewardType) constant returns(uint) {
        return reward[msg.sender][rewardType];
    }

    // Show user's current reward points
    function showMyReward(address user, bytes32 rewardType) constant returns(uint) {
        return reward[user][rewardType];
    }

    // Show user's current reward types
    function showRewardTypes(address user) constant returns(bytes32[]) {
        return availRewardTypes[user];
    }

    // Give reward after completing certain task
    function giveReward(address to, uint value, bytes32 rewardType, string rewardReasonMsg) ifOwner {
        bool availType = false;

        if (reward[msg.sender][rewardType] < value ) revert(); // Check availability
        if (reward[to][rewardType] + value < reward[to][rewardType]) revert(); // Check for
                                                            // overflows
        reward[msg.sender][rewardType] -= value;
        reward[to][rewardType] += value;

        for (uint i = 0; i < availRewardTypes[to].length; i++) {
            if (rewardType == availRewardTypes[to][i]) {
                availType = true;
                break;
            }
        }
        
        if (availType == false) {
            availRewardTypes[to].push(rewardType);
        }

         BroadcastReward(msg.sender, to, value, rewardType, "Got reward point(s).", rewardReasonMsg);
        
    }

    // Spend reward to buy items
    function spendReward(address spender, uint value, bytes32 rewardType, string spentOnMsg) ifOwner {
        address blank;
        if (reward[spender][rewardType] < value ) revert(); // Check availability
        reward[spender][rewardType] -= value;

        BroadcastReward(spender, blank, value, rewardType, "Spent reward point(s).", spentOnMsg);  
    }

    // Send own reward to other
    function sendReward(address sender, address receiver, uint value, bytes32 rewardType, string senderMsg) ifOwner {
        if (reward[sender][rewardType] < value ) revert(); // Check availability
        if (reward[receiver][rewardType] + value < reward[receiver][rewardType]) revert(); // Check
                                                                        // for
                                                                        // overflows

        reward[sender][rewardType] -= value;
        reward[receiver][rewardType] += value;

        BroadcastReward(sender, receiver, value, rewardType, "Sent reward point(s).", senderMsg);
    }

    // Set total reamaining reward to distribute.
    function setAvailRewardPoints(uint value, bytes32 rewardType) ifOwner {
        address blank;

        if (reward[msg.sender][rewardType] + value < reward[msg.sender][rewardType]) revert(); // Check
                                                                        // for
                                                                        // overflows
        reward[msg.sender][rewardType] = value;
        availRewardTypes[msg.sender].push(rewardType);    
        
        BroadcastReward(msg.sender, blank, value, rewardType, "Set new avalilable reward point(s).", "");
    }

    // Fallback function - Called if other functions don't match call or
    function () {
        revert(); // revert() reverts state to before call
    }    
}
