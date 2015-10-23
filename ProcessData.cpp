#include<iostream>
#include<fstream>
#include<string>
#include<set>

using namespace std;

int main() {
        ifstream in("map.osm");
        ofstream out("map.data");
        string aLine;
        getline(in, aLine);
        while(aLine.find("<bounds") == string::npos)
        getline(in, aLine);
        //get max/min lat&lon:
        int pos = 0, pos2 = 0;
        for(int i = 0; i < 4; i++) {
                pos = aLine.find("\"", pos);
                pos2 = aLine.find("\"", ++pos);
                out << aLine.substr(pos, pos2 - pos) << endl;
                pos = pos2 + 1;
        }
        while(aLine.find("<node") == string::npos)
        getline(in, aLine);
        //get all nodes
        set<string> NODES;
        while(true) {
                out << "node" << endl;
                pos = aLine.find("id");
                pos = aLine.find("\"", pos);
                pos2 = aLine.find("\"", ++pos);
                NODES.insert(aLine.substr(pos, pos2 - pos));
                out << aLine.substr(pos, pos2 - pos) << endl;
                
                pos = aLine.find("lat", pos);
                pos = aLine.find("\"", pos);
                pos2 = aLine.find("\"", ++pos);
                out << aLine.substr(pos, pos2 - pos) << endl;
                
                pos = aLine.find("lon", pos);
                pos = aLine.find("\"", pos);
                pos2 = aLine.find("\"", ++pos);
                out << aLine.substr(pos, pos2 - pos) << endl;
                
                if(aLine.find("/>", pos) != string::npos) {
                        out << "#" << endl;
                        getline(in, aLine);
                        if(aLine.find("<way") != string::npos)
                        break;
                        continue;
                }
                getline(in, aLine);
                while(aLine != " </node>" && aLine != "  </node>") {
                        pos = aLine.find("\"");
                        pos2 = aLine.find("\"", ++pos);
                        out << aLine.substr(pos, pos2 - pos) << endl;
                        pos = pos2 + 1;
                        pos = aLine.find("\"", pos);
                        pos2 = aLine.find("\"", ++pos);
                        out << aLine.substr(pos, pos2 - pos) << endl;
                        getline(in, aLine);
                }
                out << "#" << endl;
                getline(in, aLine);
                if(aLine.find("<way") != string::npos)
                break;
        }
        //get all roads(except those missing nodes)
        while(true) {
                out << "way" << endl;
                pos = aLine.find("\"");
                pos2 = aLine.find("\"", ++pos);
                out << aLine.substr(pos, pos2 - pos) << endl;
                
                getline(in, aLine);
                while(aLine.find("<tag") == string::npos) {
                        pos = aLine.find("\"");
                        pos2 = aLine.find("\"", ++pos);
                        if(NODES.count(aLine.substr(pos, pos2 - pos)) != 0)
                        out << aLine.substr(pos, pos2 - pos) << endl;
                        getline(in, aLine);
                }
                out << "/" << endl;
                
                while(aLine != " </way>" && aLine != "  </way>") {
                        pos = aLine.find("\"");
                        pos2 = aLine.find("\"", ++pos);
                        out << aLine.substr(pos, pos2 - pos) << endl;
                        pos = pos2 + 1;
                        pos = aLine.find("\"", pos);
                        pos2 = aLine.find("\"", ++pos);
                        out << aLine.substr(pos, pos2 - pos) << endl;
                        getline(in, aLine);
                }
                out << "#" << endl;
                getline(in, aLine);
                if(aLine.find("<way") == string::npos)
                break;
        }
        //after everything is done...
        in.close();
        out.close();
        return 0;
}
