package service;

import Requests_Results.ClearResult;

/**
 * the clear service clears the data from the database and returns the success information
 */
public class ClearService {
    /**
     * the success information of the service ( or error message)
     */
    private ClearResult result;

    /**
     *creates the Object
     */
    public ClearService() {
    }

    /**
     * clears the data and then returns the result
     * @return
     */
    public ClearResult clear(){
        return null;
    }

    public ClearResult getResult() {
        return result;
    }

    public void setResult(ClearResult result) {
        this.result = result;
    }
}
