# Reward system using Ethereum.

### For command line demo app run App.java

## Prerequisites
1. Install ethereum client (geth).

    + Mac
    
            brew tap ethereum/ethereum
            brew install ethereum
      
    + Linux (Debian)
    
            sudo apt-get install software-properties-common
            sudo add-apt-repository -y ppa:ethereum/ethereum
            sudo apt-get update
            sudo apt-get install ethereum
      
2. Install Mist Browser from [here](https://github.com/ethereum/mist/releases)
3. Note: edit all values wrapped in <> (no <> after edit)
4. Initialize Private BlockChain

        geth --identity "rewardappChain" --rpc --rpccorsdomain "*" --datadir "</full/path/to/store/blockchain>" --nodiscover --rpcapi "db,eth,net,web3" --networkid <anyIntMoreThan2> init </full/path/to/genesis.json>

5. Start Ethereum client as private net 

        geth --fast --cache 512 --ipcpath "</full/path/to/store/blockchain/rewardapp.ipc>" --identity "rewardappChain" --rpc --rpccorsdomain "*" --datadir "<dir/path/as/in/initialize>" --nodiscover --rpcapi "personal,db,eth,net,web3" --networkid <idAsInInitialize> console
    
6. Connect to Ethereum client in next terminal
    
        geth attach ipc:</full/path/to/store/blockchain/rewardapp.ipc>
    
7. Create new user & unlock it
    
        personal.newAccount("password")
        personal.unlockAccount(personal.listAccounts[0], "password")
    
8. Start a miner
    
        miner.setEtherbase(listAccounts[0]) // Set miner
        miner.start() // Start miner session
        miner.stop() // Stop miner session
    
9. Start Mist browser using (another) terminal, correct gethpath as per your system

        mist --rpc </full/path/to/store/blockchain/rewardapp.ipc> --gethpath /usr/local/bin/geth
    
10. Deploy `SmartReward/SmartReward.sol` using Mist Browser, help [here](https://www.youtube.com/watch?v=lQ4USRtzWko)
11. Edit variables `CONTRACT_ADDRESS`, `IPC_PATH`, `OWNER_KEY`, `OWNER_PASS` in `SmartReward/src/main/java/com/rewardapp/smartreward/RewardWrapper.java` as per your values
12. Run `App.java`