import './App.css';
import { useCallback, useEffect, useState } from 'react';
import Select from 'react-select'

function App() {
    const [originPlanet, setOriginPlanet] = useState(null);
    const [destinationPlanet, setDestinationPlanet] = useState({});
    const [paths, setPaths] = useState(null);
    const [planets, setPlanets] = useState([]);
    const [companies, setCompanies] = useState([]);
    const [routeMessage, setRouteMessage] = useState("");
    const [errorMsg, setErrorMsg] = useState(false);
    const [selectedCompanies, setSelectedCompanies] = useState([]);
    const [selectedFiltering, setSelectedFiltering] = useState("price");
    const [priceListValidation, setPriceListValidation] = useState(false);
    const [firstRound, setFirstRound] = useState(true);

    const test = useCallback(() => {
      fetch("http://localhost:8080/paths?originPlanet=" + originPlanet.uuid + "&destinationPlanet=" + destinationPlanet.uuid)
      .then(res => res.json())
      .then(body => setPaths(body))
      setRouteMessage(`All routes from ${originPlanet.name} to ${destinationPlanet.name}:`);
    }, [originPlanet, destinationPlanet]); 

    useEffect(() => {
      if (priceListValidation === false) {
        fetch("http://localhost:8080/planets")
        .then(res => res.json())
        .then(body => {
          setPlanets(body)
          setOriginPlanet(body[0])
          setDestinationPlanet(body[0])
          fetch("http://localhost:8080/companies")
          .then(res => res.json())
          .then(body => setCompanies(body));
          if (firstRound === false) {
            test();
          }
        });
      };
      setPriceListValidation(true);
    }, [priceListValidation, firstRound, test]);

    function selectPlanets(event) {
      setFirstRound(false);
      event.preventDefault();
      if (originPlanet.uuid === destinationPlanet.uuid) {
        setErrorMsg(true);
      } else {
        setErrorMsg(false);
      }
      fetch("http://localhost:8080/currentPriceList")
      .then(res => res.json())
      .then(body => {
        setPriceListValidation(body)
        if (body === true) {
          test();
        }
      });
    };  

    function changeOriginPlanet(event) {
      event.preventDefault();
      setOriginPlanet(JSON.parse(event.target.value));
    };

    function changeDestinationPlanet(event) {
      event.preventDefault();
      setDestinationPlanet(JSON.parse(event.target.value));
    };

    function changeSelectedCompanies(event) {
      const values = event.map(company => company.value);
      setSelectedCompanies(values);
    };

    function changeSelectedFiltering(event) {
      setSelectedFiltering(event.target.value);
    };

    function selectedFilters(event) {
      event.preventDefault();
      fetch("http://localhost:8080/filters?companies=" + selectedCompanies + "&filter=" + selectedFiltering)
      .then(res => res.json())
      .then(body => setPaths(body))
    };

  return (
    <div className="App">
        <div className="container mt-3">
            <form onSubmit={selectPlanets}>
                <div className="col-sm-2">
                    <label htmlFor="originplanet" className="form-label">Travel from:</label>
                    <select onChange={changeOriginPlanet} className="form-select" name="originplanet" id="originplanet">
                        {planets.map(planet => <option value={JSON.stringify(planet)} key={planet.uuid}>{planet.name}</option>)}
                    </select>
                    <label htmlFor="destinationplanet" className="form-label">Travel to:</label>
                    <select onChange={changeDestinationPlanet} className="form-select" name="destinationplanet" id="destinationplanet">
                        {planets.map(planet => <option value={JSON.stringify(planet)} key={planet.uuid}>{planet.name}</option>)}
                    </select>
                </div>
                <button type="submit" className="btn btn-primary mt-3">Submit</button>
            </form><br/>
        </div>
        
        {errorMsg &&
            <div className="container mt-3">
                <span style ={{color: "red"}}>"Travel from" and "Travel to" cannot be the same planet.</span>
            </div>
        }
    
        {paths !== null && errorMsg === false &&
            <div className="container mt-3">
                <form onSubmit={selectedFilters}>
                    <div className="col-sm-10">
                        <label htmlFor="companyname" className="form-label">Filter by company name</label>
                        <Select onChange={changeSelectedCompanies}
                            isMulti 
                            options={companies.map(company => ({value: company.name, label: company.name}))}/>
                            <br/>
                    </div>
                    <div className="col-sm-3">
                        <label htmlFor="options" className="form-label">Show result based on:</label>
                        <select onChange={changeSelectedFiltering} className="form-select" name="options" id="options">
                            <option value="price">Lowest price</option>
                            <option value="time">fastest travel time</option>
                        </select>
                    </div>    
                    <button type="submit" className="btn btn-primary mt-3">Submit</button>
                </form><br/>
                <span>{routeMessage}</span>
                <span>{paths.map((path, index) => <div key={index}><button type="onClick"> Route {index + 1} </button> {path}</div>)}</span><br/>
            </div>
        }
    
        <div style ={{display: "none"}}>
            <div className="container mt-3">
                <h5>To reserve your selected route, please enter your name:</h5><br/>
                <form actions="" method="post">
                    <div className="form-group">
                        <label htmlFor="firstname">First name:</label>
                        <div className="col-sm-5">
                            <input type="text" id="firstname" name="firstname" placeholder="First name" className="form-control"/>
                        </div>
                    </div>
                    <div className="form-group">
                        <label htmlFor="lastname">Last name:</label>
                        <div className="col-sm-5">
                            <input type="text" id="lastname" name="lastname" placeholder="Last name" className="form-control"/>
                        </div>
                    </div>
                    <div className="form-group">
                        <div className="col-sm-offset-2 col-sm-10">
                            <button name="register" type="submit" value="true" className="btn btn-primary mt-3">Register</button>
                        </div>
                    </div>
                </form> 
            </div>
        </div>
        <div style ={{display: "none"}}>
            <div className="container mt-3">
                <h5>Success!</h5>
                <span>Your flight has been registered.</span><br/>
                <span>Thank you for choosing our service.</span>
                <form actions="" method="post">
                    <div className="form-group">
                        <div className="col-sm-offset-2 col-sm-10">
                            <button name="backToBeginning" type="submit" value="true" className="btn btn-primary mt-3">Back to beginning</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
  );
}

export default App;